package com.parse.local.gaode;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class AmapPoi {
    @SerializedName("name")
    public String name;//搜索出的名称,es:招商银行(深圳深纺支行)

    @SerializedName("type")
    public String type;//类型,es:金融保险服务;银行;中国邮政储蓄银行"

    @SerializedName("pname")
    public String province;//省份,es：广东省

    @SerializedName("cityname")
    public String city;//市,es：深圳市


    @SerializedName("adname")
    public String district;//区：es:福田区

    @SerializedName("address")
    public String address;//详细地址，es:华强北路3号深纺大厦B座1层(华强北地铁站A口旁)


    public String getFullAddress() {
        return String.format("%s : %s : %s : %s : %s", name, province, city, district, address);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AmapPoi that = (AmapPoi) obj;
        return Objects.equals(name, that.name)
                && Objects.equals(type, that.type)
                && Objects.equals(province, that.province)
                && Objects.equals(city, that.city)
                && Objects.equals(district, that.district)
                && Objects.equals(address, that.address);
    }
}
