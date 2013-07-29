package me.rabierre.main;

import com.woorea.openstack.keystone.Keystone;
import com.woorea.openstack.keystone.model.Access;
import com.woorea.openstack.keystone.model.Tenants;

/**
 * Created with IntelliJ IDEA.
 * User: rabierre
 * Date: 13. 7. 25.
 * Time: 오후 5:54
 * To change this template use File | Settings | File Templates.
 */
public class TenantListsRequestSample {
    public static void main(String[] args) {
        Keystone client = new Keystone(SimpleConfiguration.KEYSTONE_AUTH_URL);

        Access access = client
                .tokens()
                .authenticate()
                .withUsernamePassword(SimpleConfiguration.KEYSTONE_USERNAME, SimpleConfiguration.KEYSTONE_PASSWORD)
                .withTenantName(SimpleConfiguration.TENANT_NAME)
                .execute();

        client.token(access.getToken().getId());

        Tenants tenants = client.tenants().list().execute();
        System.out.println(tenants);
    }
}
