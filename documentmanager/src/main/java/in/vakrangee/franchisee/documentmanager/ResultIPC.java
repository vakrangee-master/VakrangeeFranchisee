package in.vakrangee.franchisee.documentmanager;

import java.util.List;

public class ResultIPC {

    private static ResultIPC instance;
    private int sync = 0;
    private List<Object> largeDataList;
    private Object largeData;

    public synchronized static ResultIPC get() {
        if (instance == null) {
            instance = new ResultIPC();
        }
        return instance;
    }

    public int setLargeDataList(List<Object> largeDataList) {
        this.largeDataList = largeDataList;
        return ++sync;
    }

    public List<Object> getLargeDataList(int request) {
        return (request == sync) ? largeDataList : null;
    }

    public int setLargeData(Object largeData) {
        this.largeData = largeData;
        return ++sync;
    }

    public Object getLargeData(int request) {
        return (request == sync) ? largeData : null;
    }


}
