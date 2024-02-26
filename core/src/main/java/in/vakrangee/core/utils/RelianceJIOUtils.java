package in.vakrangee.core.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import in.vakrangee.core.model.RelianceJIOTariffPlanDto;
import in.vakrangee.core.model.ServiceProvider;

public class RelianceJIOUtils {

    public final static int SERVICE_ID = 130;

    /**
     * Add Reliance JIO Prepaid into Service List
     * @param mobileRechargeProviders
     * @return
     */
    public static List<ServiceProvider> addRelianceJIOPrepaid(List<ServiceProvider> mobileRechargeProviders) {

        int jio_sub_sub_service_id = SERVICE_ID;       // JIO
        String jio_sub_sub_service_name = "Reliance JIO Prepaid";

        // Added it into List
        mobileRechargeProviders.add(new ServiceProvider(jio_sub_sub_service_id, jio_sub_sub_service_name));

        // Sort List Based on Provider Name
        Collections.sort(mobileRechargeProviders, new Comparator<ServiceProvider>() {
            public int compare(ServiceProvider one, ServiceProvider other) {
                return one.getServiceDescription().compareTo(other.getServiceDescription());
            }
        });
        return  mobileRechargeProviders;
    }

    /**
     * Convert JSON Reliance JIO Plan
     * @param strPlans
     * @return
     */
    public static List<RelianceJIOTariffPlanDto> convertJSONToRelianceJIOPlanList(String strPlans) {
        Type listType = new TypeToken<List<RelianceJIOTariffPlanDto>>() {}.getType();
        List<RelianceJIOTariffPlanDto> relianceJIOTariffPlanDtos = new Gson().fromJson(strPlans, listType);
        relianceJIOTariffPlanDtos.add(0, new RelianceJIOTariffPlanDto("0","","Please Select",0));
        return relianceJIOTariffPlanDtos;
    }

}
