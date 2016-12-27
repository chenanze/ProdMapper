# ProdMapper V1.0.0

###  自动根据源实体类与目标实体类中的注解生成映射类,该类能将源实体类对象转换为目标实体类对象.

----

###特性:

- 自动根据注解生成原实体类提高软件可维护性
- 使用简单连接两个类并转换只需添加 3 行代码
- 使用基于注解的预编译技术生成的代码速度可媲美手写代码(无性能损耗)
- 可使用表达式自定义自动生成的映射类的名称
- (可选)提供使用方便的统一API,使用前无需 `Make` 项目(使用反射技术有一定性能损耗)
- 支持源类中的内部类的`List`对象的嵌套解析
- 用户友好的编译时错误提示和警告

### 安装:

在 module 层级的 `build.gradle` 下

```groovy
apply plugin: 'com.neenbedankt.android-apt'
...
dependencies {
    compile 'com.chenanze:prodmapper:1.0.0'
    compile 'com.chenanze:prodmapper-annotation:1.0.0'
    compile 'com.chenanze:prodmapper-compiler:1.0.0'
}
```

### 使用:

#### 注解待 Map 源类:

在 Mapper 源实体类中的待 Map 类名称上(允许静态内部类)加入 `@BindType` 注解和 `Get` 方法以及待生成的**Mapper**类名称,**支持使用表达式指定 Mapper 类名称**:

其中**$O**代表**源类**名称**$T**代表**目标类**名称 (**不指定名称则使用默认名称**).

**正则表达式**: `^(.*)(\\$[O|T])(.*)(\\$[O|T])(.*)$`

```java
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

     public TabEntity(List<DatasBean> datas) {
         Datas = datas;
     }

     public void setDatas(List<DatasBean> Datas) {
         this.Datas = Datas;
     }

     public static class DatasBean {
         public DatasBean(List<ProjTabBean> proj_tab) {
             this.proj_tab = proj_tab;
         }

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

         @BindType(value = Tab.class,proxyClassName = "$O$T")
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

                 public ProjTabBean(String iconFileName, String iconFileUrlLink, String actionMode, String actionParam, String id, boolean isEnable, String name, int orderno, String project_id, String remark) {
                     this.iconFileName = iconFileName;
                     this.iconFileUrlLink = iconFileUrlLink;
                     this.actionMode = actionMode;
                     this.actionParam = actionParam;
                     this.id = id;
                     this.isEnable = isEnable;
                     this.name = name;
                     this.orderno = orderno;
                     this.project_id = project_id;
                     this.remark = remark;
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

             private boolean isIsEnable() {
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
```



#### 注解待 Map 目标类:

在待 Map 目标类构造器处加入 `@Construction`注解

```java
public class Tab extends UIComponent implements Serializable {
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

```



#### 预编译生成 Mapper 类:

请确保**源类**的`get` 方法名与**目标类**方法名称在符合驼峰命名规范的前提下一致.

执行`make`操作,**Mapper**类将会在路径:

`**projectName**/**moduleName**/build/generated/source/apt/debug/com.prodmapper` 下生成



#### 使用自动生成的 Mapper 类:

```java
TabEntity tabEntity = new TabEntity(
            new ArrayList<DatasBean>() {{
                add(new DatasBean(
                        new ArrayList<DatasBean.ProjTabBean>() {{
                            add(new DatasBean.ProjTabBean("iconFileName", "iconFileUrlLink", "actionMode", "actionParam", "id", false, "TestName", 0, "0", "remark"));
                            add(new DatasBean.ProjTabBean("iconFileName1", "iconFileUrlLink1", "actionMode1", "actionParam1", "id1", false, "TestName1", 1, "1", "remark1"));
                            add(new DatasBean.ProjTabBean("iconFileName2", "iconFileUrlLink2", "actionMode2", "actionParam2", "id2", false, "TestName2", 2, "2", "remark2"));
                            add(new DatasBean.ProjTabBean("iconFileName3", "iconFileUrlLink3", "actionMode3", "actionParam3", "id3", false, "TestName3", 3, "3", "remark3"));
                        }}));
                add(new DatasBean(
                        new ArrayList<DatasBean.ProjTabBean>() {{
                            add(new DatasBean.ProjTabBean("iconFileName4", "iconFileUrlLink4", "actionMode4", "actionParam4", "id4", false, "TestName4", 4, "4", "remark4"));
                            add(new DatasBean.ProjTabBean("iconFileName5", "iconFileUrlLink5", "actionMode5", "actionParam5", "id5", false, "TestName5", 5, "5", "remark5"));
                            add(new DatasBean.ProjTabBean("iconFileName6", "iconFileUrlLink6", "actionMode6", "actionParam6", "id6", false, "TestName6", 6, "6", "remark6"));
                            add(new DatasBean.ProjTabBean("iconFileName7", "iconFileUrlLink7", "actionMode7", "actionParam7", "id7", false, "TestName7", 7, "7", "remark7"));
                        }}));
                add(new DatasBean(
                        new ArrayList<DatasBean.ProjTabBean>() {{
                            add(new DatasBean.ProjTabBean("iconFileName8", "iconFileUrlLink8", "actionMode8", "actionParam8", "id8", false, "TestName8", 8, "8", "remark8"));
                            add(new DatasBean.ProjTabBean("iconFileName9", "iconFileUrlLink9", "actionMode9", "actionParam9", "id9", false, "TestName9", 9, "9", "remark9"));
                            add(new DatasBean.ProjTabBean("iconFileName10", "iconFileUrlLink10", "actionMode10", "actionParam10", "id10", false, "TestName10", 10, "10", "remark10"));
                            add(new DatasBean.ProjTabBean("iconFileName11", "iconFileUrlLink11", "actionMode11", "actionParam11", "id11", false, "TestName11", 11, "11", "remark11"));
                        }}));
            }});

// 使用生成的目标类需 make 项目无性能损耗
List<Tab> tabList = ProjTabBeanTab.transform(tabEntity);
// 使用 API 使用前无需 make 项目,但使用反射,有性能损耗
List<Tab> tabList = (List<Tab>) Prodmapper.transfroms(tabEntity, TabEntity.DatasBean.ProjTabBean.class);
```
