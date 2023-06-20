package com.wakefern.api.proxy.wakefern.prodx;

public class ProdxUtils {

    private final static String DEFAULT_CORPORATE_STORE = "3000";
    private final static String CONVERTED_CORPORATE_STORE = "0163";

    //Convert the default corporate store to a specific store before calling Prodx
    public static String storeIdConvertor(String id) {
        String wakefernStoreId = null;

        if (id.equalsIgnoreCase(DEFAULT_CORPORATE_STORE)) {
            wakefernStoreId = CONVERTED_CORPORATE_STORE;
        } else switch (id.length()) {
            case 1:
                wakefernStoreId = "000" + id;
                break;
            case 2:
                wakefernStoreId = "00" + id;
                break;
            case 3:
                wakefernStoreId = "0" + id;
                break;
            case 4:
                wakefernStoreId = id;
                break;
            default:
                throw new RuntimeException("A Wakefern store ID has to be btw 0 and 9999");

        }

        return wakefernStoreId;
    }
}
