package chenanze.com.prodmapper;

/**
 * Created by duian on 2016/9/26.
 */

public class UIComponent {
    private String id;
    private String name;
    private String remark;

    public UIComponent(String id, String name, String remark) {
        this.id = id;
        this.name = name;
        this.remark = remark;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
