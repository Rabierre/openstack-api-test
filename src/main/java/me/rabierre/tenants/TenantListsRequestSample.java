package me.rabierre.tenants;

import com.woorea.openstack.keystone.Keystone;
import com.woorea.openstack.keystone.model.Access;
import com.woorea.openstack.keystone.model.Tenant;
import com.woorea.openstack.keystone.model.Tenants;
import me.rabierre.SimpleConfiguration;

public class TenantListsRequestSample {
    public static void main(String[] args) {
        Keystone client = new Keystone(SimpleConfiguration.KEYSTONE_PUBLIC_URL);

        Access access = client
                .tokens()
                .authenticate()
                .withUsernamePassword(SimpleConfiguration.KEYSTONE_USERNAME, SimpleConfiguration.KEYSTONE_PASSWORD)
                .execute();

        client.token(access.getToken().getId());

        // GET http://10.0.1.3:5000/v2.0/tenants
        Tenants tenants = client.tenants().list().execute();

        for (Tenant tenant : tenants) {
            System.out.println(tenant.toString());
        }
    }
}
