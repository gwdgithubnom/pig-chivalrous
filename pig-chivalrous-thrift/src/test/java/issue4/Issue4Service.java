package issue4;

import org.gjgr.pig.chivalrous.thrift.test.User;

public interface Issue4Service {

    public ReturnResult<User> getUser(int userId); 
}
