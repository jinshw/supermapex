1，编译后的Jar放到 %SuperMap iServer Java_HOME%/webapps/iserver/WEB-INF/lib 目录

2，配置 Interface
iserver-services-interfaces.xml 文件（位于%SuperMap iServer Java_HOME%/webapps/iserver/WEB-INF/） 中新增一个 interface 节点，内容如下：
<!--Temperature Interface-->
<interface name="temperatureInterface" class="com.supermap.sample.temperature.TemperatureServlet">
</interface>

3，配置 Provider
配置 temperatureProvider， SuperMap iServer 默认的 iserver-services.xml 中已经配置了 ugcMapProvider-China400。
<!-- Temperature Provider-->
<provider name="temperatureProvider" class="com.supermap.sample.temperature.TemperatureProvider">
    <config class="com.supermap.sample.temperature.FileSetting">
        <filePath>../Temperature.txt</filePath>
    </config>
</provider>
根据配置，Temperature.txt 文件需要放置到 %SueprMap iServer Java_HOME% 目录下。
配置 ProviderSet
iserver-services.xml 中配置 providerSet 节点，内容如下：
<!-- Temperature providerSet -->
<providerSet name="TemperatureProviderset">
    <provider-reference name="ugcMapProvider-China400" enabled="true"></provider-reference>
    <provider-reference name="temperatureProvider" enabled="true"></provider-reference>
</providerSet>

4，配置 Component
iserver-services.xml 文件中修改  Temperature component 节点，interfaceNames 设为 "temperatureInterface"，内容如下：
<!-- Temperature component -->
<component name="Temperature" class="com.supermap.sample.temperature.Temperature" providers="TemperatureProviderset" interfaceNames="temperatureInterface">
    <config class="com.supermap.sample.temperature.TemperatureParam">
        <mapName>China</mapName>
    </config>
</component>
说明：这里服务接口的配置设为 interfaceNames="temperatureInterface"。

5，访问 http://localhost:8090/iserver/services/Temperature/temperatureInterface，输入“北京”，点击提交


