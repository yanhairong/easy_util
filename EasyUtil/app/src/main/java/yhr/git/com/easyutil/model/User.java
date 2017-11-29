package yhr.git.com.easyutil.model;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Description:
 *
 * @author YanHaiRong
 * @version 2.0
 * @since 2017/11/23
 */
public class User extends DataSupport implements Serializable{


    String name;

    String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
