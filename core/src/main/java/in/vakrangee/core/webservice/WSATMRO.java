package in.vakrangee.core.webservice;

import java.util.HashMap;

/**
 * Created by Nileshd on 6/6/2017.
 */
public interface WSATMRO {

    String getATMList(HashMap<String, String> parameters, String method_name_my_vakangee_kendra, String urlMyVakrangeeKendra, String soapAction, String NameSpace);

    String getAtmRoCashLoadingRecipts(HashMap<String, String> parameters, String method_name_my_vakangee_kendra, String urlMyVakrangeeKendra, String soapAction, String NameSpace);

    String getAddAtmRoCashLoading(HashMap<String, String> parameters, String method_name_my_vakangee_kendra, String urlMyVakrangeeKendra, String soapAction, String NameSpace);

    String getDenomination(HashMap<String, String> parameters, String method_name_my_vakangee_kendra, String urlMyVakrangeeKendra, String soapAction, String NameSpace);

    String getRetailOutletList(HashMap<String, String> parameters, String method_name_my_vakangee_kendra, String urlMyVakrangeeKendra, String soapAction, String NameSpace);


    String createROAcknowlegement(HashMap<String, String> parameters, String method_name_my_vakangee_kendra, String urlMyVakrangeeKendra, String soapAction, String NameSpace);

}
