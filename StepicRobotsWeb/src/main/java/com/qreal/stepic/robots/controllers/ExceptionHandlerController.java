/*
 * Copyright Vladimir Zakharov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.qreal.stepic.robots.controllers;

import com.qreal.stepic.robots.exceptions.SubmitException;
import com.qreal.stepic.robots.exceptions.UploadException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Created by vladimir-zakharov on 12.08.15.
 */
@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(SubmitException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public byte[] handleSubmitException(SubmitException e) {
        e.printStackTrace();
        return e.getMessage().getBytes(UTF_8);
    }

    @ExceptionHandler(UploadException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public byte[] handleUploadException(UploadException e) {
        e.printStackTrace();
        return e.getMessage().getBytes(UTF_8);
    }

    @ExceptionHandler({ NoHandlerFoundException.class, NoSuchRequestHandlingMethodException.class })
    public String handleNotFoundException(Exception e) {
        e.printStackTrace();
        return "errors/404";
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception e) {
        e.printStackTrace();
        ModelAndView modelAndView = new ModelAndView("errors/common");
        modelAndView.addObject("message", "An error occurred. Please contact the developers.");
        return modelAndView;
    }



}
