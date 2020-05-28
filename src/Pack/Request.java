package Pack;

import java.io.Serializable;

public class Request implements Serializable {
    private int type;
    //1 - закрытие коннекта
    //2 - запрос на получение коллекции от другого клиента
    //3 - запрос на передачу коллекции другому клиенту
    //4 - запрос на получение коллекции от текущего объекта
    //5 - запрос на передачу коллекции текущему объекту
    //6 - запрос на передачу коллекции соединений
    private Object data;
    private int destination;
    public Request(int type){
        this.type=type;
    }

    public void setDestination(int destination) {
        this.destination = destination;
    }

    public int getDestination() {
        return destination;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
