package me.rabierre.servers;

import com.woorea.openstack.base.client.OpenStackSimpleTokenProvider;
import com.woorea.openstack.keystone.Keystone;
import com.woorea.openstack.keystone.model.Access;
import com.woorea.openstack.keystone.model.Tenants;
import com.woorea.openstack.nova.Nova;
import com.woorea.openstack.nova.model.*;
import me.rabierre.SimpleConfiguration;

public class ServerCreateRequestSample {
    public static void main(String[] args) {
        Keystone keystone = new Keystone(SimpleConfiguration.KEYSTONE_PUBLIC_URL);
        Access access = keystone
                .tokens()
                .authenticate()
                .withUsernamePassword(SimpleConfiguration.KEYSTONE_USERNAME, SimpleConfiguration.KEYSTONE_PASSWORD)
                .execute();

        keystone.token(access.getToken().getId());

        Tenants tenants = keystone.tenants().list().execute();

        if (tenants.getList().size() > 0) {

            access = keystone.tokens().authenticate()
                    .withToken(access.getToken().getId())
                    .withTenantId(tenants.getList().get(0).getId()).execute();

            Nova nova = new Nova(SimpleConfiguration.NOVA_COMPUTE_URL.concat("/").concat(tenants
                    .getList().get(0).getId()));
            nova.setTokenProvider(new OpenStackSimpleTokenProvider(access.getToken().getId()));

            KeyPairs keysPairs = nova.keyPairs().list().execute();

            Images images = nova.images().list(true).execute();

            Flavors flavors = nova.flavors().list(true).execute();

            ServerForCreate serverForCreate = new ServerForCreate();
            serverForCreate.setName("woorea");
            serverForCreate.setFlavorRef(flavors.getList().get(6).getId());
            serverForCreate.setImageRef(images.getList().get(0).getId());
            serverForCreate.setKeyName(keysPairs.getList().size() != 0 ? keysPairs.getList().get(0).getName() : "");
            serverForCreate.getSecurityGroups()
                    .add(new ServerForCreate.SecurityGroup("default"));

            Server server = nova.servers().boot(serverForCreate).execute();
            System.out.println(server);

        } else {
            System.out.println("No tenants found!");
        }
    }
}
