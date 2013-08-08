package me.rabierre.servers;

import com.woorea.openstack.keystone.Keystone;
import com.woorea.openstack.keystone.model.Access;
import com.woorea.openstack.keystone.model.authentication.UsernamePassword;
import com.woorea.openstack.nova.Nova;
import com.woorea.openstack.nova.model.Servers;
import me.rabierre.SimpleConfiguration;

public class ServerVNCConsoleRequestSample {
    public static void main(String[] args) {
        Keystone keystone = new Keystone(SimpleConfiguration.KEYSTONE_PUBLIC_URL);
        Access access = keystone.tokens().authenticate(new UsernamePassword(SimpleConfiguration.KEYSTONE_USERNAME, SimpleConfiguration.KEYSTONE_PASSWORD))
                .withTenantName(SimpleConfiguration.TENANT_DEMO)
                .execute();

        keystone.token(access.getToken().getId());

        Nova novaClient = new Nova(SimpleConfiguration.NOVA_COMPUTE_URL.concat("/").concat(access.getToken().getTenant().getId()));
        novaClient.token(access.getToken().getId());

        Servers servers = novaClient.servers().list(true).execute();

        novaClient.servers().getVncConsole(servers.getList().get(0).getId(), "novnc").execute();
    }
}
