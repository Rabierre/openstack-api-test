package me.rabierre.networks;

import com.woorea.openstack.base.client.OpenStackSimpleTokenProvider;
import com.woorea.openstack.keystone.Keystone;
import com.woorea.openstack.keystone.model.Access;
import com.woorea.openstack.keystone.model.Tenants;
import com.woorea.openstack.keystone.model.authentication.TokenAuthentication;
import com.woorea.openstack.keystone.model.authentication.UsernamePassword;
import com.woorea.openstack.keystone.utils.KeystoneUtils;
import com.woorea.openstack.quantum.Quantum;
import com.woorea.openstack.quantum.model.Network;
import com.woorea.openstack.quantum.model.Networks;
import me.rabierre.SimpleConfiguration;

public class NetworkListRequestSample {
    public static void main(String[] args) {
        Keystone keystone = new Keystone(SimpleConfiguration.KEYSTONE_PUBLIC_URL);
        Access access = keystone.tokens().authenticate(
                new UsernamePassword(SimpleConfiguration.KEYSTONE_USERNAME, SimpleConfiguration.KEYSTONE_PASSWORD))
                .execute();

        keystone.setTokenProvider(new OpenStackSimpleTokenProvider(access.getToken().getId()));

        Tenants tenants = keystone.tenants().list().execute();

        if (tenants.getList().size() > 0) {
            access = keystone.tokens()
                    .authenticate(new TokenAuthentication(access.getToken().getId())).withTenantId(tenants.getList().get(0).getId())
                    .execute();

            Quantum quantum = new Quantum(SimpleConfiguration.QUANTUM_NETWORK_URL);
            quantum.setTokenProvider(new OpenStackSimpleTokenProvider(access.getToken().getId()));

            Networks networks = quantum.networks().list().execute();
            for (Network network : networks) {
                System.out.println(network);
            }
        } else {
            System.out.println("No tenants found!");
        }
    }
}