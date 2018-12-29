package com.supermap.sample.position;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.supermap.services.Interface;
import com.supermap.services.InterfaceContext;
import com.supermap.services.InterfaceContextAware;
import com.supermap.services.components.commontypes.Geometry;
import com.supermap.services.components.commontypes.GeometrySpatialAnalystResult;
import com.supermap.services.rest.util.JsonConverter;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.*;

@Interface(componentTypes = {MileagePosition.class}, optional = false, multiple = false)
public class MileagePositionServlet extends HttpServlet implements InterfaceContextAware {
    private String id = null;
    private MileagePosition mileagePosition = null;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out;

        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        out = response.getWriter();

        String title = "输入查询的城市名称";
        out.println("<html>");
        out.println("<head>");
        out.println("<TITLE>" + title + "</TITLE>");
        out.println("</head>");

        out.println("<body>");
        out.println("<p align=\"center\">" + "欢迎进行城市天气查询" + "</p>");
        out.println("<hr>");
        out.println("<form method=\"POST\" action=>");
        out.println("<p align=\"center\">");
        out.println("<label>道路编码：");
        out.println("<input type=\"text\" name=\"roadcode\" id=\"roadcode\" size=\"20\">");
        out.println("</label>");
        out.println("<label>起始桩号：");
        out.println("<input type=\"text\" name=\"startzh\" id=\"startzh\" size=\"20\">");
        out.println("</label>");
        out.println("<label>终点桩号：");
        out.println("<input type=\"text\" name=\"endzh\" id=\"endzh\" size=\"20\">");
        out.println("</label>");
        out.println("</p>");
        out.println("<p align=\"center\">");
        out.println("<label>");
        out.println("<input type=\"submit\" name=\"submit\" value=\"提交\">");
        out.println("</label>");
        out.println("<label>");
        out.println("<input type=\"reset\" name=\"reset\" value=\"重置\">");
        out.println("</label>");
        out.println("</p>");

        out.println("</form>");
        out.println("</body>");
        out.println("</html>");

        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        JSONObject result = new JSONObject();
        String result = "";
        List list = null;
        GeometrySpatialAnalystResult geometrySpatialAnalystResult = null;
        request.setCharacterEncoding("UTF-8");
        String roadtype = request.getParameter("roadtype");
        String roadcode = request.getParameter("roadcode");
        String startzh = request.getParameter("startzh");
        String endzh = request.getParameter("endzh");
        String requestType = request.getParameter("requestType");

        if (requestType != null) {
            if(requestType.equals("zdxx")){// 阻断信息
                list = mileagePosition.getZDXXList();
            }
        } else { // 里程桩定位
            Map<String, String> map = new HashMap<String, String>();
            if (startzh != null && endzh != null && !"".equals(startzh) && !"".equals(endzh)) {
                map.put("roadtype", roadtype);
                map.put("roadcode", roadcode);
                map.put("startzh", startzh);
                map.put("endzh", endzh);
                list = mileagePosition.getFeaturesOfMileagePosition(map);
            } else {
                map.put("roadtype", roadtype);
                map.put("roadcode", roadcode);
                if (startzh != null && !"".equals(startzh)) {
                    map.put("measure", startzh);
                } else if (endzh != null && !"".equals(endzh)) {
                    map.put("measure", endzh);
                }
                map.put("offset", "0");
                map.put("isIgnoreGap", "1");
                geometrySpatialAnalystResult = mileagePosition.locatePoint(map);
                list = new ArrayList();
                if (geometrySpatialAnalystResult != null) {
                    list.add(geometrySpatialAnalystResult);
                }
            }
        }


        com.supermap.services.rest.util.JsonConverter js = new JsonConverter();
        result = js.toFormatedObject(list).toString();
        String cb = request.getParameter("callback");
        if (cb != null) {
            //jsonp格式处理，应对js跨域问题
            result = cb + "(" + result + ")";
            response.setContentType("text/javascript");
        } else {
            response.setContentType("application/x-json;charset=utf-8");
        }
        PrintWriter out = response.getWriter();
        out.write(result);
        out.close();
    }

    @Override
    public void setInterfaceContext(InterfaceContext context) {
        this.getServletContext().setAttribute(this.id + "InterfaceContext", context);
        List<Object> components = context.getComponents(Object.class);
        if (components != null) {
            for (Object component : components) {
                if (component instanceof MileagePosition) {
                    this.mileagePosition = (MileagePosition) component;
                    break;
                }
            }
        }
    }
}
