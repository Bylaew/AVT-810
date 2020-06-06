import java.io.Serializable;

public class Package implements Serializable {
    enum Request_type{
        REQUEST,
        CLIENTS_RECEIVE,
        ANSWER
    }
    private Object data;
    private Request_type request_type;
    private int destination;
    public int sender = 0;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Package(Request_type request_type ){
        this.request_type = request_type;
    }

    public Request_type getRequest_type() {
        return request_type;
    }
    public void setDestination(int destination) {
        this.destination = destination;
    }
    public int getDestination() {
        return destination;
    }
}
