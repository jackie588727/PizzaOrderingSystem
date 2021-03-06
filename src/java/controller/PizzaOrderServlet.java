/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Pizza;

/**
 *
 * @author Jackie
 */
public class PizzaOrderServlet extends HttpServlet {

    /**
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.sendRedirect("index.jsp");

    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (request.getSession().getAttribute("cart") == null) {
            request.getSession().setAttribute("cart", new ArrayList<Pizza>());
        }

        //get the exsiting cart
        ArrayList<Pizza> cart = (ArrayList<Pizza>) request.getSession().getAttribute("cart");
        Pizza pizza = new Pizza();

        int qty = 0;
        try {
            qty = Integer.parseInt(request.getParameter("qty"));
        } catch (NumberFormatException ex) {
            System.err.print("Numberformation Error: " + ex);
        }
        //set delivery method
        boolean delivery = (request.getParameter("delivery").equals("true")) ? true : false;
        pizza.setDelivery(delivery);
        //set pizza size
        pizza.setSize(request.getParameter("pizzasize"));
        //adding toppings
        if (request.getParameterValues("toppings") != null) {
            for (String t : request.getParameterValues("toppings")) {
                pizza.addTopping(t);
            }
        }
        //calculate the price of the pizza
        boolean exist = false;
        pizza.getToppingCount();
        if (cart.size() > 0) {  //if theres a pizza order
            for (int i = 0; i < cart.size(); i++) {
                Pizza p = cart.get(i);  //get the pizza in arraylist
                if (p.equals(pizza)) {  //check if its the same pizza
                    p.setQty(p.getQty() + qty); //set Qty
                    p.calPrice();   //cal price of total
                    exist = true;
                    break;
                }
            }
            //no pizza order
        }

        if (cart.size() <= 0 || !exist) {
            pizza.setQty(qty);
            pizza.calPrice();
            //add to cart
            cart.add(pizza);
        }

        //re set the cart
        request.getSession().setAttribute("cart", cart);

        RequestDispatcher view = request.getRequestDispatcher("order.jsp");
        view.forward(request, response);

    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
