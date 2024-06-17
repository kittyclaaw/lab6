package global.models;

import java.io.Serializable;

public class Response implements Serializable {
    private final String massage;

    public Response (String massage){
        this.massage = massage;
    }

    private Object responseObj;

    public Response(String s, Object obj) {
        massage = s;
        responseObj = obj;
    }

    @Override
    public String toString() {
        return massage;
    }
}
