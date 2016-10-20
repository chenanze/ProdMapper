package chenanze.com.prodmapper;

import com.chenanze.prodmapper.Construction;

import java.io.Serializable;

/**
 * Created by duian on 2016/9/26.
 */

public class Tab extends UIComponent implements Serializable {

    /**
     * iconFileName : 预警报警
     * iconFileUrlLink : 5bf6dfb1-c973-4444-815c-09c24b3ed195.png
     * isEnable : true
     * orderno : 1
     * project_id : bc4d12075746983a0157469a2db300c9
     */

    private String iconFileName;
    private String iconFileUrlLink;
    private String actionMode;
    private String actionParam;
    private boolean isEnable;
    private int orderno;
    private String project_id;

    @Construction
    public Tab(String id, String name, String remark, String iconFileName, String iconFileUrlLink, String actionMode, String actionParam, boolean isEnable, int orderno, String project_id) {
        super(id, name, remark);
        this.iconFileName = iconFileName;
        this.iconFileUrlLink = iconFileUrlLink;
        this.actionMode = actionMode;
        this.actionParam = actionParam;
        this.isEnable = isEnable;
        this.orderno = orderno;
        this.project_id = project_id;
    }

    public String getIconFileName() {
        return iconFileName;
    }

    public void setIconFileName(String iconFileName) {
        this.iconFileName = iconFileName;
    }

    public String getIconFileUrlLink() {
        return iconFileUrlLink;
    }

    public String getActionMode() {
        return actionMode;
    }

    public void setActionMode(String actionMode) {
        this.actionMode = actionMode;
    }

    public String getActionParam() {
        return actionParam;
    }

    public void setActionParam(String actionParam) {
        this.actionParam = actionParam;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    public void setIconFileUrlLink(String iconFileUrlLink) {
        this.iconFileUrlLink = iconFileUrlLink;
    }

    public boolean isIsEnable() {
        return isEnable;
    }

    public void setIsEnable(boolean isEnable) {
        this.isEnable = isEnable;
    }

    public int getOrderno() {
        return orderno;
    }

    public void setOrderno(int orderno) {
        this.orderno = orderno;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    @Override
    public String toString() {
        return "Tab{" +
                "iconFileName='" + iconFileName + '\'' +
                ", iconFileUrlLink='" + iconFileUrlLink + '\'' +
                ", actionMode='" + actionMode + '\'' +
                ", actionParam='" + actionParam + '\'' +
                ", isEnable=" + isEnable +
                ", orderno=" + orderno +
                ", project_id='" + project_id + '\'' +
                '}';
    }
}
