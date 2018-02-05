package com.javens.mq.impl;

import com.javens.mq.BusinessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class BusinessServiceImpl implements BusinessService{
    protected static final Logger logger = LoggerFactory.getLogger(BusinessServiceImpl.class);

    public boolean execute() {
        logger.info("do execute...");
        return false;
    }

    public boolean check() {
        logger.info("do check...");
        return false;
    }
}
