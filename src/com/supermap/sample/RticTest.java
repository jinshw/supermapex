package com.supermap.sample;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.supermap.sample.traffic.*;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class RticTest {
    public static Map rttRoadRelMap = new HashMap();

    public static void main(String[] args) {
        getXML();

    }

    public static void getXML() {
        System.out.println("开始......");
        String areaCodes = "650100,650200,650400,650500,652300,652700,652800,652900,653000,653100,653200,654000,654200,654300";
        String rticUrl = "http://newte.sh.1251225243.clb.myqcloud.com/TEGateway/123456/RTICTraffic.xml?bizcode=xjjtysxx6bc624asdb98asdhjdf12&version=1701&datatype=14&format=1&adcode={0}&kind={1}";
        String roadKind = "5";
        String rttXmlString = "";
        for (String areaCode : areaCodes.split(",")) {
            String urlString = MessageFormat.format(rticUrl, areaCode, roadKind);
            rttXmlString = rttXmlString + "开始------"+areaCode+"-------------"+areaCode+"------------------------------------------------------------------------------------------------------------------------------------------------------------";
            rttXmlString = rttXmlString +  CommUtil.getDataByUrl(urlString);
            rttXmlString = rttXmlString + "结束------"+areaCode+"======"+areaCode+"=======================-------------------------------------------------------------------------------------------------------------------------";

        }
        FileUtils.writeStringToFile("e:\\xml.xml",rttXmlString);
        System.out.println("结束......");
    }


    public Map<String, Object> getRttByAdcodeJson(String rticUrl, String areaCode, String kindLevel) {
        Map<String, Object> map = new TreeMap<String, Object>();

        JSONArray rttRoadRelJsonArray = null;

        if (rttRoadRelMap.size() == 0) {
            //初次加载，获取完成后存入缓存
            String path = "E:\\supermap-iserver-9.0.1a-win64-zip\\webapps\\iserver\\WEB-INF\\classes\\";
//        String path = this.getClass().getClassLoader().getResource(".").getPath();
            String urlPath = CommUtil.rttRelSel(path + "rtic", areaCode);
            String rttRoadRelJsonStr = FileUtils.readFile(urlPath, "UTF-8");
            rttRoadRelMap = rttRoadRelJsonToMap(rttRoadRelJsonStr);
        }



       /* if (!StringUtils.isEmpty(rttRoadRelJsonStr)) {
            //jsonStr转换json
            rttRoadRelJsonArray = JSONArray.parseArray(rttRoadRelJsonStr);
        } else {
            System.out.println("路网关系表获取失败：" + areaCode);
        }*/

        //第二步：通过接口获取实时路况数据
        String urlString = MessageFormat.format(rticUrl, areaCode, kindLevel);
        //"http://newte.sh.1251225243.clb.myqcloud.com/TEGateway/123456/RTICTraffic.xml?bizcode=xjjtysxx6bc624asdb98asdhjdf12&version=1701&datatype=14&format=1&adcode=" + areaCode+"&kind="+kindLevel;
        String rttXmlString = CommUtil.getDataByUrl(urlString);
        RticTest rticTest = new RticTest();

        List<XmlBean> list = rticTest.xmlToObject(rttXmlString);
        try {
            list = getRDatas(list);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * XML数据转成Bean
     *
     * @param xml
     * @return
     */
    public List<XmlBean> xmlToObject(String xml) {
        List<XmlBean> list = new ArrayList<XmlBean>();
        XmlBean xmlBean = null;
        // 解析books.xml文件
        // 创建SAXReader的对象reader
        SAXReader reader = new SAXReader();
        try {
            // 通过reader对象的read方法加载books.xml文件,获取docuemnt对象。
            Document document = reader.read(new ByteArrayInputStream(xml.getBytes()));
//            Document document = reader.read(new File("C:\\Users\\Administrator\\Desktop\\xml.xml"));

            // 通过document对象获取根节点bookstore
            Element root = document.getRootElement();
            // 通过element对象的elementIterator方法获取迭代器
            Iterator it = root.elementIterator();
            // 遍历迭代器，获取根节点中的信息 response
            while (it.hasNext()) {//response
                Element respEle = (Element) it.next();
                String status = respEle.elementTextTrim("status"); // 子节点status值
                System.out.println("status:" + status);
                Iterator resultIt = respEle.elementIterator();
                while (resultIt.hasNext()) {//cities
                    Element citiesEle = (Element) resultIt.next();
                    Iterator citiesIt = citiesEle.elementIterator();
                    while (citiesIt.hasNext()) {//city
                        Element cityEle = (Element) citiesIt.next();
                        Iterator cityIt = cityEle.elementIterator();
                        while (cityIt.hasNext()) {//mesh
                            Element meshEle = (Element) cityIt.next();
                            Iterator meshIt = meshEle.elementIterator();
                            String code = meshEle.elementTextTrim("code");
                            System.out.println("code------" + code);
                            if (code == null) {
                                continue;
                            }

                            while (meshIt.hasNext()) {//rtic
                                Element rticEle = (Element) meshIt.next();
                                Iterator rticIt = rticEle.elementIterator();
                                String rticId = rticEle.attributeValue("id");
                                String kind = rticEle.attributeValue("kind");
                                String clazz = rticEle.attributeValue("class");
                                String length = rticEle.attributeValue("length");
//                                System.out.println("rticId==" + rticId + "--kind==" + kind);
                                /**
                                 * 只匹配 0：高速道路，1：快速道路
                                 * kind:0：高速道路，1：快速道路，2：一般道路，3：其他道路
                                 */
                                if (rticId != null && kind != null &&
                                        (kind.equals("0") || kind.equals("1"))
                                        ) {

                                    while (rticIt.hasNext()) {//flow
                                        Element flowEle = (Element) rticIt.next();
                                        Iterator flowIt = flowEle.elementIterator();
                                        String flowStartlen = flowEle.elementTextTrim("startlen");
                                        String flowTraveltime = flowEle.elementTextTrim("traveltime");
                                        String flowLos = flowEle.elementTextTrim("los");
//                                        System.out.println("startlen=" + startlen + "--traveltime=" + traveltime + "---los=" + los);

                                        // 获取拥堵数据：los拥堵等级，0：未知，1：畅通，2：缓慢，3：拥堵
                                        if (flowLos.equals("3")) {
                                            System.out.println("flowStartlen=" + flowStartlen + "--flowTraveltime=" + flowTraveltime + "---flowLos=" + flowLos + "rticId==" + rticId + "--kind==" + kind + "--clazz==" + clazz + "--length=" + length);
                                            xmlBean = new XmlBean();
                                            xmlBean.setCode(code);
                                            xmlBean.setRticId(rticId);
                                            xmlBean.setKind(kind);
                                            xmlBean.setLength(length);
                                            xmlBean.setClazz(clazz);
                                            xmlBean.setFlowStartlen(flowStartlen);
                                            xmlBean.setFlowLos(flowLos);
                                            xmlBean.setFlowTraveltime(flowTraveltime);
                                            list.add(xmlBean);

                                            /*while (flowIt.hasNext()) {//sectionlist
                                                Element sectionlistEle = (Element) flowIt.next();
                                                Iterator sectionlistIt = sectionlistEle.elementIterator();
                                                while (sectionlistIt.hasNext()) {//section
                                                    Element sectionEle = (Element) sectionlistIt.next();
                                                    String startlenSection = sectionEle.elementTextTrim("startlen");
                                                    String degreelenSection = sectionEle.elementTextTrim("degreelen");
                                                    String losSection = sectionEle.elementTextTrim("los");
                                                    xmlBean = new XmlBean();
                                                    xmlBean.setCode(code);
                                                    xmlBean.setRticId(rticId);
                                                    xmlBean.setKind(kind);
                                                    xmlBean.setLength(length);
                                                    xmlBean.setClazz(clazz);
                                                    xmlBean.setFlowStartlen(flowStartlen);
                                                    xmlBean.setFlowLos(flowLos);
                                                    xmlBean.setFlowTraveltime(flowTraveltime);
                                                    xmlBean.setSectionStartlen(startlenSection);
                                                    xmlBean.setSectionDegreelen(degreelenSection);
                                                    xmlBean.setSectionLos(losSection);
                                                    list.add(xmlBean);
                                                    System.out.println("startlenSection=" + startlenSection + "--degreelenSection=" + degreelenSection + "---losSection=" + losSection);
                                                }
                                            }*/


                                        }
                                    }
                                }
                            }

                        }
                    }
                }
            }
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println("list.size===" + list.size());
        return list;
    }

    /**
     * 获取导航路网数据
     *
     * @param list 实时路况接口返回数据
     * @return
     */
    public List<XmlBean> getRDatas(List<XmlBean> list) throws InvocationTargetException, IllegalAccessException {
        List<XmlBean> rlist = new ArrayList<XmlBean>();
        XmlBean xmlBeanNew = null;
        Iterator iterator = list.iterator();
        XmlBean xmlBean = null;
        while (iterator.hasNext()) {
            xmlBean = (XmlBean) iterator.next();
            String rttcLinkID = xmlBean.getCode() + StrUtil.leftAppend(5, xmlBean.getRticId());
            String rticLinkKind = xmlBean.getKind();
            JSONArray jsonArray = (JSONArray) rttRoadRelMap.get(rttcLinkID + rticLinkKind);
            Iterator jsonArrayIt = jsonArray.iterator();
            while (jsonArrayIt.hasNext()) {
                JSONObject jsonObject = (JSONObject) jsonArrayIt.next();
                xmlBeanNew = new XmlBean();
                BeanUtils.copyProperties(xmlBeanNew, xmlBean);
                xmlBeanNew.setRticLinkID(rttcLinkID);
                xmlBeanNew.setRticLinkKind(rticLinkKind);
                xmlBeanNew.setNiLinkID(jsonObject.getString("NILinkID"));
                xmlBeanNew.setNiLinkLength(jsonObject.getString("NILinkLength"));
                xmlBeanNew.setLinkId(jsonObject.getString("NILinkID"));
                rlist.add(xmlBeanNew);
            }
        }
        return rlist;
    }

    /**
     * 路况与路网关系转换成Map
     *
     * @param content 关系文本
     * @return
     */
    public Map rttRoadRelJsonToMap(String content) {
        Map map = new HashMap();
        JSONArray rttRoadRelJsonArray = JSONArray.parseArray(content);
        Iterator iterator = rttRoadRelJsonArray.iterator();
        while (iterator.hasNext()) {
            JSONObject jsonObject = (JSONObject) iterator.next();
            String linkKind = jsonObject.getString("RticLinkKind");
            String linkID = jsonObject.getString("RTIC_linkID");
            JSONArray includeArr = (JSONArray) jsonObject.get("include");
            map.put(linkID + linkKind, includeArr);
        }
        return map;
    }

}


