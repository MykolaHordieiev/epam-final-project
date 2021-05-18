package org.test.project.operator;

import lombok.RequiredArgsConstructor;
import org.test.project.User.User;
import org.test.project.User.UserRole;
import org.test.project.infra.web.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
public class OperatorController {

    private final OperatorService operatorService;



}
