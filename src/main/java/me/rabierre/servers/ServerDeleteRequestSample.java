package me.rabierre.servers;

import com.woorea.openstack.keystone.Keystone;
import com.woorea.openstack.keystone.model.Access;
import com.woorea.openstack.keystone.model.authentication.UsernamePassword;
import com.woorea.openstack.nova.Nova;
import com.woorea.openstack.nova.model.Servers;
import me.rabierre.SimpleConfiguration;

/**
 * Created with IntelliJ IDEA.
 * User: rabierre
 * Date: 13. 7. 30.
 * Time: 오후 5:37
 * To change this template use File | Settings | File Templates.
 */
public class ServerDeleteRequestSample {
    public static void main(String[] args) {
        Keystone keystone = new Keystone(SimpleConfiguration.KEYSTONE_PUBLIC_URL);
        Access access = keystone.tokens().authenticate(new UsernamePassword(SimpleConfiguration.KEYSTONE_USERNAME, SimpleConfiguration.KEYSTONE_PASSWORD))
                .withTenantName("demo")
                .execute();

        //use the token in the following requests
        keystone.token(access.getToken().getId());

        Nova novaClient = new Nova(SimpleConfiguration.NOVA_COMPUTE_URL.concat("/").concat(access.getToken().getTenant().getId()));
        novaClient.token(access.getToken().getId());

        Servers servers = novaClient.servers().list(true).execute();
        novaClient.servers().delete(servers.getList().get(0).getId()).execute();
    }
}
