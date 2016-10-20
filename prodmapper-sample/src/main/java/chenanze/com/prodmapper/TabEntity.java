package chenanze.com.prodmapper;

import com.chenanze.prodmapper.BindType;

import java.util.List;

/**
 * Created by duian on 2016/9/26.
 */
public class TabEntity {

    private List<DatasBean> Datas;

    public List<DatasBean> getDatas() {
        return Datas;
    }

    public void setDatas(List<DatasBean> Datas) {
        this.Datas = Datas;
    }

    public static class DatasBean {
        /**
         * iconFileName : 预警报警
         * iconFileUrlLink : 5bf6dfb1-c973-4444-815c-09c24b3ed195.png
         * id : bc4d1207574b870b01574b92b5400065
         * isEnable : true
         * name : 预警报警
         * orderno : 1
         * project_id : bc4d12075746983a0157469a2db300c9
         * remark :
         */

        private List<ProjTabBean> proj_tab;

        public List<ProjTabBean> getProj_tab() {
            return proj_tab;
        }

        public void setProj_tab(List<ProjTabBean> proj_tab) {
            this.proj_tab = proj_tab;
        }

        @BindType(Tab.class)
        public static class ProjTabBean {
            private String iconFileName;
            private String iconFileUrlLink;
            private String actionMode;
            private String actionParam;
            private String id;
            public boolean isEnable;
            private String name;
            private int orderno;
            private String project_id;
            private String remark;

            public String getIconFileName() {
                return iconFileName;
            }

            public void setIconFileName(String iconFileName) {
                this.iconFileName = iconFileName;
            }

            public String getIconFileUrlLink() {
                return iconFileUrlLink;
            }

            public void setIconFileUrlLink(String iconFileUrlLink) {
                this.iconFileUrlLink = iconFileUrlLink;
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

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            private boolean getIsEnable() {
                return isEnable;
            }

            public void setIsEnable(boolean isEnable) {
                this.isEnable = isEnable;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
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

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            @Override
            public String toString() {
                return "ProjTabBean{" +
                        "iconFileName='" + iconFileName + '\'' +
                        ", iconFileUrlLink='" + iconFileUrlLink + '\'' +
                        ", actionMode='" + actionMode + '\'' +
                        ", actionParam='" + actionParam + '\'' +
                        ", id='" + id + '\'' +
                        ", isEnable=" + isEnable +
                        ", name='" + name + '\'' +
                        ", orderno=" + orderno +
                        ", project_id='" + project_id + '\'' +
                        ", remark='" + remark + '\'' +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "DatasBean{" +
                    "proj_tab=" + proj_tab +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "TabEntity{" +
                "Datas=" + Datas +
                '}';
    }
}
