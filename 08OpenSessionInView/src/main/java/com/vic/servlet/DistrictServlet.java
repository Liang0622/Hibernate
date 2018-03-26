package com.vic.servlet;

import com.vic.dao.DistrictDao;
import com.vic.dao.DistrictDaoImpl;
import com.vic.pojo.District;
import com.vic.service.DistrictService;
import com.vic.service.DistrictServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/findById")
public class DistrictServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        DistrictService service=new DistrictServiceImpl();//实例化service
        District district=service.getDistrictById(3);
        req.setAttribute("d",district);
        req.getRequestDispatcher("index.jsp").forward(req,resp);
    }
}
