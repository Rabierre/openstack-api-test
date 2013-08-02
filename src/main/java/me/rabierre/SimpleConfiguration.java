package me.rabierre;

/**
 * Created with IntelliJ IDEA.
 * User: rabierre
 * Date: 13. 7. 25.
 * Time: 오후 5:48
 * To change this template use File | Settings | File Templates.
 */
public class SimpleConfiguration {

    public static final String KEYSTONE_USERNAME = "admin";

    public static final String KEYSTONE_PASSWORD = "dkclagottkf";

    // Identity Service & Admin
    public static final String KEYSTONE_PUBLIC_URL = "http://10.0.1.3:5000/v2.0";

    public static final String KEYSTONE_ADMIN_URL = "http://10.0.1.3:35357/v2.0";

    public static final String TENANT_ADMIN = "admin";

    public static final String TENANT_DEMO = "demo";

    // Compute - handle Instances
    public static final String NOVA_COMPUTE_URL = "http://10.0.1.3:8774/v2";    // not v2.0!
    // Glance - handle Images
    public static final String GLANCE_IMAGE_URL = "http://10.0.1.3:9292/v1";    // v2 need more than Image class
}
