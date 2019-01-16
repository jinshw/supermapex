package com.supermap.sample.traffic;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.beanutils.BeanUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.ByteArrayInputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.*;
import java.util.Map.Entry;

public class RticUtils {
    public static Map rttRoadRelMap = new HashMap();
    // 标识映射文件是否已经读取
    public static Map<String, Boolean> rttRoadIsReadMap = new HashMap<String, Boolean>();
    //    public static String PATH = "E:\\supermap-iserver-9.0.1a-win64-zip\\webapps\\iserver\\WEB-INF\\classes\\";
    public static String PATH = RticUtils.class.getResource("/").getPath();

    public static void main(String[] args) {
        PATH = "E:\\supermap-iserver-9.0.1a-win64-zip\\webapps\\iserver\\WEB-INF\\classes\\";
        String rticUrl = "http://newte.sh.1251225243.clb.myqcloud.com/TEGateway/123456/RTICTraffic.xml?bizcode=xjjtysxx6bc624asdb98asdhjdf12&version=1701&datatype=14&format=1&adcode={0}&kind={1}";
        String roadKind = "5";
        String areaCodes = "650100";

        RticUtils rticTest = new RticUtils();
        rticTest.getLinkList(rticUrl, areaCodes, roadKind);

    }

    /**
     * 获取拥堵路况和路网数据
     *
     * @param rticUrl   实时路况
     * @param areaCode  城市区域编码
     * @param kindLevel 获取等级
     * @return
     */
    public static Map<String, List<XmlBean>> getLinkList(String rticUrl, String areaCode, String kindLevel) {

        if (rttRoadRelMap.size() == 0 || rttRoadIsReadMap.get(areaCode) == null || !rttRoadIsReadMap.get(areaCode)) {
            //初次加载，获取完成后存入缓存
//            String path = "E:\\supermap-iserver-9.0.1a-win64-zip\\webapps\\iserver\\WEB-INF\\classes\\";
//            String path = RticUtils.class.getResource(".").getPath();
            String urlPath = CommUtil.rttRelSel(PATH + "rtic", areaCode);
//            System.out.println("urlPath====" + PATH + "rtic-----areaCode=" + areaCode);
            rttRoadIsReadMap.put(areaCode, true);
            String rttRoadRelJsonStr = FileUtils.readFile(urlPath, "UTF-8");
            rttRoadRelMap.putAll(rttRoadRelJsonToMap(rttRoadRelJsonStr));
        }

        //第二步：通过接口获取实时路况数据
        String urlString = MessageFormat.format(rticUrl, areaCode, kindLevel);
        String rttXmlString = CommUtil.getDataByUrl(urlString);
        Map<String, List<XmlBean>> map = xmlToObject(rttXmlString);
        List<XmlBean> list = map.get("jam");
//        List<XmlBean> list = xmlToObject(rttXmlString);
        try {
            list = getRDatas(list);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        map.put("jam", list);
        return map;

    }

    /**
     * XML数据转成Bean
     *
     * @param xml
     * @return
     */
    public static Map<String, List<XmlBean>> xmlToObject(String xml) {
        Map<String, List<XmlBean>> map = new HashMap<>();
        List<XmlBean> jamList = new ArrayList<XmlBean>();
        List<XmlBean> allList = new ArrayList<XmlBean>();

        XmlBean xmlBean = null;
        List<String> repeatList = new ArrayList<>();//存储 code + rticId + kind
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

                                        if (code.equals("662151")) {
                                            System.out.println(code + rticId + kind);
                                        }

                                        // 获取拥堵数据：los拥堵等级，0：未知，1：畅通，2：缓慢，3：拥堵
                                        String onlyKey = code + rticId + kind;// 避免重复数据
//                                        if (flowLos != null && flowLos.equals("3") && !repeatList.contains(onlyKey)) {
                                        if (flowLos != null && !repeatList.contains(onlyKey)) {
                                            xmlBean = new XmlBean();
                                            xmlBean.setCode(code);
                                            xmlBean.setRticId(rticId);
                                            xmlBean.setKind(kind);
                                            xmlBean.setLength(length);
                                            xmlBean.setClazz(clazz);
                                            xmlBean.setFlowStartlen(flowStartlen);
                                            xmlBean.setFlowLos(flowLos);
                                            xmlBean.setFlowTraveltime(flowTraveltime);
                                            if (flowLos.equals("3")) {
                                                jamList.add(xmlBean);// 拥堵路段列表
                                            }
                                            allList.add(xmlBean);// 所有等级路段列表拥堵等级，0：未知，1：畅通，2：缓慢，3：拥堵
                                            repeatList.add(onlyKey);

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
        map.put("all", allList);
        map.put("jam", jamList);
        return map;
    }

    /**
     * 获取导航路网数据
     *
     * @param list 实时路况接口返回数据
     * @return
     */
    public static List<XmlBean> getRDatas(List<XmlBean> list) throws InvocationTargetException, IllegalAccessException {
        List<XmlBean> rlist = new ArrayList<XmlBean>();
        XmlBean xmlBeanNew = null;
        Iterator iterator = list.iterator();
        XmlBean xmlBean = null;
        while (iterator.hasNext()) {
            xmlBean = (XmlBean) iterator.next();
            String rttcLinkID = xmlBean.getCode() + StrUtil.leftAppend(5, xmlBean.getRticId());
            String rticLinkKind = xmlBean.getKind();
            JSONArray jsonArray = (JSONArray) rttRoadRelMap.get(rttcLinkID + rticLinkKind);

            if (jsonArray != null) {
                xmlBeanNew = new XmlBean();
                BeanUtils.copyProperties(xmlBeanNew, xmlBean);
                xmlBeanNew.setRticLinkID(rttcLinkID);
                xmlBeanNew.setRticLinkKind(rticLinkKind);
                xmlBeanNew.setInclude(jsonArray);
                rlist.add(xmlBeanNew);
                /*
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
                }*/
            } else {
//                System.out.println("PATH==" + RticUtils.PATH);
                //System.out.println("rttcLinkID==" + rttcLinkID + "   rticLinkKind==" + rticLinkKind);
            }

        }
        return rlist;
    }

    public static Map<String, String> getAllToKeyMapByLos(List<XmlBean> list) throws InvocationTargetException, IllegalAccessException {
        Map<String, String> map = new HashMap<>();
        List<XmlBean> rlist = new ArrayList<XmlBean>();
        XmlBean xmlBeanNew = null;
        Iterator iterator = list.iterator();
        XmlBean xmlBean = null;
        while (iterator.hasNext()) {
            xmlBean = (XmlBean) iterator.next();
            String rttcLinkID = xmlBean.getCode() + StrUtil.leftAppend(5, xmlBean.getRticId());
            String rticLinkKind = xmlBean.getKind();
            JSONArray jsonArray = (JSONArray) rttRoadRelMap.get(rttcLinkID + rticLinkKind);

            if (jsonArray != null) {
                Iterator jsonArrayIt = jsonArray.iterator();
                while (jsonArrayIt.hasNext()) {
                    JSONObject jsonObject = (JSONObject) jsonArrayIt.next();
                    String losTemp = map.get(jsonObject.getString("NILinkID"));
                    if(losTemp == null ||  "3".equals(xmlBean.getFlowLos())){
                        map.put(jsonObject.getString("NILinkID"), xmlBean.getFlowLos());
                    }else{
                        System.out.println("NILinkID===="+jsonObject.getString("NILinkID")+"重复了"+"  losTemp="+losTemp);
                    }

//                    map.put(jsonObject.getString("NILinkID"), xmlBean.getFlowLos());
                }
                /*xmlBeanNew = new XmlBean();
                BeanUtils.copyProperties(xmlBeanNew, xmlBean);
                xmlBeanNew.setRticLinkID(rttcLinkID);
                xmlBeanNew.setRticLinkKind(rticLinkKind);
                xmlBeanNew.setInclude(jsonArray);
                rlist.add(xmlBeanNew);*/
            } else {
//                System.out.println("PATH==" + RticUtils.PATH);
            }

        }
        return map;
    }

    /**
     * 路况与路网关系转换成Map
     *
     * @param content 关系文本
     * @return
     */
    public static Map rttRoadRelJsonToMap(String content) {
        Map map = new HashMap();
        try {
            JSONArray rttRoadRelJsonArray = JSONArray.parseArray(content);
            Iterator iterator = rttRoadRelJsonArray.iterator();
            while (iterator.hasNext()) {
                JSONObject jsonObject = (JSONObject) iterator.next();
                String linkKind = jsonObject.getString("RticLinkKind");
                String linkID = jsonObject.getString("RTIC_linkID");
                JSONArray includeArr = (JSONArray) jsonObject.get("include");
                map.put(linkID + linkKind, includeArr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 排序（按照length从大到小）
     *
     * @param list XmlBean的List集合
     * @return
     */
    public static List<XmlBean> sortListBylength(List<XmlBean> list) {
        Collections.sort(list, new Comparator<XmlBean>() {
            @Override
            public int compare(XmlBean o1, XmlBean o2) {
                int lenOne = Integer.valueOf(o1.getLength());
                int lenTow = Integer.valueOf(o2.getLength());
                if (lenOne < lenTow) {
                    return 1;
                } else if (lenOne == lenTow) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });
        return list;
    }

    /**
     * 去除重复数据
     *
     * @param list
     * @return
     */
    public static List<XmlBean> distinctList(List<XmlBean> list) {
        List<XmlBean> rlist = new ArrayList<>();
        Map<String, XmlBean> map = new HashMap<>();
        Iterator<XmlBean> iterator = list.iterator();
        XmlBean xmlBean = null;
        while (iterator.hasNext()) {
            xmlBean = iterator.next();
            String rttcLinkID = xmlBean.getCode() + StrUtil.leftAppend(5, xmlBean.getRticId());
            String rticLinkKind = xmlBean.getKind();
            String key = rttcLinkID + rticLinkKind;
            map.put(key, xmlBean);
        }

        for (String keyIndex : map.keySet()) {
            rlist.add(map.get(keyIndex));
        }
        return rlist;
    }

}


