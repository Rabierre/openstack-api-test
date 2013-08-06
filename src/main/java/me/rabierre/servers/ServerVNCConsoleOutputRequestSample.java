package me.rabierre.servers;

import com.woorea.openstack.keystone.Keystone;
import com.woorea.openstack.keystone.model.Access;
import com.woorea.openstack.keystone.model.authentication.UsernamePassword;
import com.woorea.openstack.nova.Nova;
import com.woorea.openstack.nova.api.ServersResource;
import com.woorea.openstack.nova.model.Server;
import com.woorea.openstack.nova.model.ServerAction;
import com.woorea.openstack.nova.model.Servers;
import me.rabierre.SimpleConfiguration;

/**
 * Created with IntelliJ IDEA.
 * User: rabierre
 * Date: 13. 7. 30.
 * Time: 오후 5:37
 * To change this template use File | Settings | File Templates.
 */
public class ServerVNCConsoleOutputRequestSample {
    public static void main(String[] args) {
        Keystone keystone = new Keystone(SimpleConfiguration.KEYSTONE_PUBLIC_URL);
        Access access = keystone.tokens().authenticate(new UsernamePassword(SimpleConfiguration.KEYSTONE_USERNAME, SimpleConfiguration.KEYSTONE_PASSWORD))
                .withTenantName(SimpleConfiguration.TENANT_DEMO)
                .execute();

        //use the token in the following requests
        keystone.token(access.getToken().getId());

        Nova novaClient = new Nova(SimpleConfiguration.NOVA_COMPUTE_URL.concat("/").concat(access.getToken().getTenant().getId()));
        novaClient.token(access.getToken().getId());

        Servers servers = novaClient.servers().list(true).execute();
        Server server = servers.getList().get(0);

        novaClient.servers().getConsoleOutput(server.getId(), 50).execute();

    }
}
