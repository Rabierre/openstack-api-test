package me.rabierre.main;

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

    public static final String TENANT_NAME = "admin";

    // Compute
    public static final String COMPUTE_URL = "http://10.0.1.3:9696/v2.0";
}
