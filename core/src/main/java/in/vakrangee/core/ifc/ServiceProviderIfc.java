package in.vakrangee.core.ifc;

import java.util.List;

import in.vakrangee.core.model.ServiceProvider;

/**
 * Created by Nileshd on 4/18/2016.
 */
public interface ServiceProviderIfc {

    /**
     * This method is used to call ServiceProvider and Sub service id pass to server for Mobile and DTH recharge.
     * @param serviceId
     * @param subServiceId
     * @return
     */

     List<ServiceProvider> getServiceProvider(int serviceId, int subServiceId);

    /**
     * This method is used to call getAvailableBalance in your account.
     * @param output
     *
     * @return
     */

    void getAvailableBalance(String output);


}
