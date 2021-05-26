package org.test.project.infra.web;

public class ExceptionHandlerImplMy implements ExceptionHandler {

    @Override
    public ModelAndView handle(Exception exception) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView("/error/ex.jsp");
        if (exception instanceof MyException) {
            modelAndView.addAttribute("message", exception.getMessage());
            return modelAndView;
        }
        return ModelAndView.withView("/error/internalerror.jsp");
    }
}
