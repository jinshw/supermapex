package com.supermap.sample;

import com.supermap.sample.traffic.XmlBean;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.awt.print.Book;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Dom4JTest {
    private static ArrayList<Book> bookList = new ArrayList<Book>();

    /**
     * @param args
     */
    public static void main(String[] args) {
        List<XmlBean> list = new ArrayList<XmlBean>();
        XmlBean xmlBean = null;
        // 解析books.xml文件
        // 创建SAXReader的对象reader
        SAXReader reader = new SAXReader();
        try {
            // 通过reader对象的read方法加载books.xml文件,获取docuemnt对象。
            Document document = reader.read(new File("C:\\Users\\Administrator\\Desktop\\xml.xml"));

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
                                            while (flowIt.hasNext()) {//sectionlist
                                                Element sectionlistEle = (Element) flowIt.next();
                                                Iterator sectionlistIt = sectionlistEle.elementIterator();
                                                while (sectionlistIt.hasNext()) {//section
                                                    Element sectionEle = (Element) sectionlistIt.next();
                                                    String startlenSection = sectionEle.elementTextTrim("startlen");
                                                    String degreelenSection = sectionEle.elementTextTrim("degreelen");
                                                    String losSection = sectionEle.elementTextTrim("los");
                                                    xmlBean = new XmlBean();
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
                                            }
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

        System.out.println("list.size==="+list.size());
    }
}
