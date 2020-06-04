import java.io.Serializable;

public class Request implements Serializable
{
    int operationID;
    // 0 - подключение нового клиента и передача ему данных о клиентах
    // 1 - передача данных клиентам о новом клиенте
    // 2 - отключение клиента
    // 3 - передача данных между клиентами
    Object data;
    String receiver;

    Request(int ID)
    {
        this.operationID = ID;
    }

    Request(int ID, Object data)
    {
        this.operationID = ID;
        this.data = data;
    }

    Request(int ID, Object data, String receiver)
    {
        this.operationID = ID;
        this.data = data;
        this.receiver = receiver;
    }
}

class DataPack implements Serializable
{
    int N1;
    double P1;
    int N2;
    long ORLifetime;
    long ALifetime;

    DataPack(int N1, double P1, int N2, long ORLifetime, long ALifetime)
    {
        this.N1 = N1;
        this.P1 = P1;
        this.N2 = N2;
        this.ORLifetime = ORLifetime;
        this.ALifetime = ALifetime;
    }
}