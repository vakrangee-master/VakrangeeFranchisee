package in.vakrangee.core.impl;

import android.database.Cursor;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

import in.vakrangee.core.ifc.ServiceProviderIfc;
import in.vakrangee.core.model.ServiceProvider;
import in.vakrangee.core.utils.Connection;

/**
 * Created by Nileshd on 4/18/2016.
 */
public class ServiceProviderImpl implements ServiceProviderIfc {

    public String fieldObjectName = "field12";

    @Override
    public List<ServiceProvider> getServiceProvider(int serviceId, int subServiceId) {

        String sqlQuery = "select sub_sub_service_id,sub_sub_service_name  from sub_sub_service_master where service_id='" + serviceId + "'" +
                " and sub_service_id ='" + subServiceId + "' order by sub_sub_service_name ";

        Cursor cursor = null;
        Log.d("", sqlQuery);

        List<ServiceProvider> serviceList = new LinkedList<ServiceProvider>();
        try {
            serviceList.add(new ServiceProvider(0, "Please Select"));
            cursor = Connection.VKMSDatabase.rawQuery(sqlQuery, null);
            if (cursor != null && cursor.getCount() != 0) {
                while (cursor.moveToNext()) {
                    serviceList.add(new ServiceProvider(Integer.valueOf(cursor.getString(0)), cursor.getString(1)));
                }
            }
        } catch (Exception e) {
            return serviceList;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return serviceList;
    }

    @Override
    public void getAvailableBalance(String output) {
        String aa = output;
    }


    public String getCurrentStatus() {

        String sqlQuery = "select Status from FranchiseeMaster";
        Cursor cursor = null;
        Log.d("", sqlQuery);

        try {
            cursor = Connection.VKMSDatabase.rawQuery(sqlQuery, null);
            if (cursor != null && cursor.getCount() != 0) {
                while (cursor.moveToNext()) {
                    return cursor.getString(0);
                }
            }
        } catch (Exception e) {
            return "NA";
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return "NA";
    }

    public boolean isTokenId() {

        String sqlQuery = "select TokenId from FranchiseeMaster";
        Cursor cursor = null;
        Log.d("", sqlQuery);

        try {
            cursor = Connection.VKMSDatabase.rawQuery(sqlQuery, null);
            if (cursor != null && cursor.getCount() != 0) {
                while (cursor.moveToNext()) {
                    return true;
                }
            }
        } catch (Exception e) {
            return false;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return false;
    }


    public List<ServiceProvider> getAccountStatement() {
        String sqlQuery = "select SubServiceId,SubServiceName from WalletServicesMaster order by SubServiceName";

        Cursor cursor = null;
        Log.d("", sqlQuery);

        List<ServiceProvider> serviceList = new LinkedList<ServiceProvider>();
        try {
            // serviceList.add(new ServiceProvider(0, "Please select"));
            cursor = Connection.VKMSDatabase.rawQuery(sqlQuery, null);
            if (cursor != null && cursor.getCount() != 0) {
                while (cursor.moveToNext()) {
                    serviceList.add(new ServiceProvider(Integer.valueOf(cursor.getString(0)), cursor.getString(1)));
                }
            }
        } catch (Exception e) {
            return serviceList;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return serviceList;
    }

    public List<ServiceProvider> getMyTransactions() {
        String sqlQuery = "select SubServiceId,SubServiceName from WalletServicesMaster  where SubServiceId ='11' or SubServiceId='5' or  SubServiceId='6' order by SubServiceName";

        Cursor cursor = null;
        Log.d("", sqlQuery);

        List<ServiceProvider> serviceList = new LinkedList<ServiceProvider>();
        try {
            // serviceList.add(new ServiceProvider(0, "Please select"));
            cursor = Connection.VKMSDatabase.rawQuery(sqlQuery, null);
            if (cursor != null && cursor.getCount() != 0) {
                while (cursor.moveToNext()) {
                    serviceList.add(new ServiceProvider(Integer.valueOf(cursor.getString(0)), cursor.getString(1)));
                }
            }
        } catch (Exception e) {
            return serviceList;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return serviceList;
    }


}
