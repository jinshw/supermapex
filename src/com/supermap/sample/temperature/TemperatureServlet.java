package com.supermap.sample.temperature;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.supermap.services.Interface;
import com.supermap.services.InterfaceContext;
import com.supermap.services.InterfaceContextAware;

@Interface(componentTypes = { Temperature.class }, optional = false, multiple = false)
public class TemperatureServlet extends HttpServlet implements InterfaceContextAware {

    private String id = null;
    private Temperature temperature = null;

    public TemperatureServlet() {
        this.id = String.valueOf(System.currentTimeMillis());
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
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
        out.println("<label>城市名称：");
        out.println("<input type=\"text\" name=\"cityname\" id=\"cityname\" size=\"20\">");
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

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String parameter = request.getParameter("cityname");
        String cityName = URLDecoder.decode(parameter, "UTF-8");
        String imageURI = temperature.getMapImage(cityName);

        // 对服务器IP和端口处理
        String port = String.valueOf(request.getServerPort());
        imageURI = imageURI.replace("{port}", port);
        imageURI = imageURI.replace("{ip}", request.getServerName());

        String info = temperature.getTemperature(cityName);
        PrintWriter out;

        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        out = response.getWriter();
        String title = "天气信息";

        out.println("<html>");
        out.println("<head>");
        out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
        out.println("<TITLE>" + cityName + title + "</TITLE>");
        out.println("</head>");

        out.println("<body>");
        out.println("<p align=\"center\">" + cityName + "的天气是：" + info + "</p>");
        out.println("<p align=\"center\">");
        out.println("<img src=" + imageURI + ">");
        out.println("</p>");
        out.println("</body>");
        out.println("</html>");
    }

    public void setInterfaceContext(InterfaceContext context) {
        this.getServletContext().setAttribute(this.id + "InterfaceContext", context);

        List<Object> components = context.getComponents(Object.class);
        if (components != null) {
            for (Object component : components) {
                if (component instanceof Temperature) {
                    this.temperature = (Temperature) component;
                    break;
                }
            }
        }
    }
}
