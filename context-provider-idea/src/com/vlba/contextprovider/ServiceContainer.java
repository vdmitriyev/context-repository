package com.vlba.contextprovider;

/**
 * Created by hamdi on 13/08/15.
 */
public class ServiceContainer {


    private int id;
    private String name,desc;
    private int perm;


    public ServiceContainer(int id, String name, String desc, int perm) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.perm = perm;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getPerm() {
        return perm;
    }

    public void setPerm(int perm) {
        this.perm = perm;
    }

    @Override
    public String toString() {
        return "ServiceContainer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", perm=" + perm +
                '}';
    }
}
