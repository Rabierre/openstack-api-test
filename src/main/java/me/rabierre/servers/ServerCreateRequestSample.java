package me.rabierre.servers;

import com.woorea.openstack.base.client.OpenStackSimpleTokenProvider;
import com.woorea.openstack.keystone.Keystone;
import com.woorea.openstack.keystone.model.Access;
import com.woorea.openstack.keystone.model.Tenants;
import com.woorea.openstack.keystone.model.authentication.UsernamePassword;
import com.woorea.openstack.nova.Nova;
import com.woorea.openstack.nova.api.ServersResource;
import com.woorea.openstack.nova.model.*;
import me.rabierre.SimpleConfiguration;

/**
 * Created with IntelliJ IDEA.
 * User: rabierre
 * Date: 13. 7. 30.
 * Time: 오후 5:37
 * To change this template use File | Settings | File Templates.
 */
public class ServerCreateRequestSample {
    public static void main(String[] args) {
        Keystone keystone = new Keystone(SimpleConfiguration.KEYSTONE_PUBLIC_URL);
        // access with unscoped token
        Access access = keystone
                .tokens()
                .authenticate()
                .withUsernamePassword(SimpleConfiguration.KEYSTONE_USERNAME, SimpleConfiguration.KEYSTONE_PASSWORD)
                .execute();

        // use the token in the following requests
        keystone.token(access.getToken().getId());

        Tenants tenants = keystone.tenants().list().execute();

        // try to exchange token using the first tenant
        if (tenants.getList().size() > 0) {

            access = keystone.tokens().authenticate()
                    .withToken(access.getToken().getId())
                    .withTenantId(tenants.getList().get(0).getId()).execute();

            // NovaClient novaClient = new
            // NovaClient(KeystoneUtils.findEndpointURL(access.getServiceCatalog(),
            // "compute", null, "public"), access.getToken().getId());

            Nova nova = new Nova(SimpleConfiguration.NOVA_COMPUTE_URL.concat("/").concat(tenants
                    .getList().get(0).getId()));
            nova.setTokenProvider(new OpenStackSimpleTokenProvider(access.getToken().getId()));

            // create a new keypair
            // KeyPair keyPair = novaClient.execute(KeyPairsExtension.createKeyPair("mykeypair"));

            // create security group
            // SecurityGroup securityGroup =
            // novaClient.execute(SecurityGroupsExtension.createSecurityGroup("mysecuritygroup", "description"));

            // novaClient.execute(SecurityGroupsExtension.createSecurityGroupRule(securityGroup.getId(),"UDP", 9090, 9092, "0.0.0.0/0"));
            // novaClient.execute(SecurityGroupsExtension.createSecurityGroupRule(securityGroup.getId(),"TCP", 8080, 8080, "0.0.0.0/0"));

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
            // serverForCreate.getSecurityGroups().add(new ServerForCreate.SecurityGroup(securityGroup.getName()));

            Server server = nova.servers().boot(serverForCreate).execute();
            System.out.println(server);

        } else {
            System.out.println("No tenants found!");
        }
    }
}
