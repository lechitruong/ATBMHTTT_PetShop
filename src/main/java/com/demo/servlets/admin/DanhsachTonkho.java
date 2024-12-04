package com.demo.servlets.admin;

import java.io.IOException;
import java.sql.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.demo.entities.Pets;
import com.demo.models.PetModel;

@WebServlet("/admin/quanlitonkho")
public class DanhsachTonkho extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public DanhsachTonkho() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PetModel petModel = new PetModel();

        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");
        String filterStatus = request.getParameter("filterStatus");

        List<Pets> pets;
        if (startDateStr != null && endDateStr != null && filterStatus != null &&
                !startDateStr.isEmpty() && !endDateStr.isEmpty() && !filterStatus.isEmpty()) {
            Date startDate = Date.valueOf(startDateStr);
            Date endDate = Date.valueOf(endDateStr);
            if ("no-orders".equals(filterStatus)) {
                pets = petModel.findInactivePetsInRange(startDate, endDate);
            } else {
                pets = petModel.findActivePetsInRange(startDate, endDate);
            }
        } else {
            pets = petModel.findAll();
        }

        request.setAttribute("petinventory", pets);
        request.setAttribute("p", "../admin/showinventory.jsp");
        request.getRequestDispatcher("/WEB-INF/views/layout/admin.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
