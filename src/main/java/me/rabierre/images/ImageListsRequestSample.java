package me.rabierre.images;

import com.woorea.openstack.glance.Glance;
import com.woorea.openstack.glance.model.Image;
import com.woorea.openstack.glance.model.Images;
import com.woorea.openstack.keystone.Keystone;
import com.woorea.openstack.keystone.model.Access;
import com.woorea.openstack.keystone.model.Tenants;
import com.woorea.openstack.keystone.model.authentication.TokenAuthentication;
import com.woorea.openstack.keystone.model.authentication.UsernamePassword;
import me.rabierre.SimpleConfiguration;

public class ImageListsRequestSample {
    public static void main(String[] args) {
        Keystone keystone = new Keystone(SimpleConfiguration.KEYSTONE_PUBLIC_URL);
        Access access = keystone.tokens().authenticate(
                new UsernamePassword(SimpleConfiguration.KEYSTONE_USERNAME, SimpleConfiguration.KEYSTONE_PASSWORD))
                .execute();

        keystone.token(access.getToken().getId());

        Tenants tenants = keystone.tenants().list().execute();

        if(tenants.getList().size() > 0) {

            access = keystone.tokens().authenticate(
                    new TokenAuthentication(access.getToken().getId()))
                    .withTenantId(tenants.getList().get(0).getId())
                    .execute();

            // GET http://10.0.1.3:9292/v1/images
            Glance glanceClient = new Glance(SimpleConfiguration.GLANCE_IMAGE_URL);
            glanceClient.token(access.getToken().getId());
            Images images = glanceClient.images().list(false).execute();

            for(Image image : images) {
                System.out.println(image.toString());
            }

        } else {
            System.out.println("No tenants found!");
        }
    }
}
