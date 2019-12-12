package issue6;

import java.util.List;

import org.gjgr.pig.chivalrous.thrift.test.Status;

public interface Issue6Service {

    public List<Status> getUserStatus(List<Status> us); 
}
