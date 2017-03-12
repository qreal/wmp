package com.qreal.wmp.db.diagram.client.longpoll;

import com.qreal.wmp.thrift.gen.LongpollThriftService;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TJSONProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service("longpollService")
@PropertySource("classpath:client.properties")
public class LongpollServiceImpl implements LongpollService {
    private static final Logger logger = LoggerFactory.getLogger(LongpollServiceImpl.class);

    private THttpClient transport;

    private LongpollThriftService.Client client;

    @Value("${port.longpoll}")
    private int port;

    @Value("${path.longpoll}")
    private String path;

    @Value("${path.longpoll.thrift}")
    private String pathThrift;

    /** Creates a connection with Thrift TServer.*/
    @PostConstruct
    public void start() throws InterruptedException, TTransportException {
        String resultPath = "http://" + path + ":" + String.valueOf(port) + pathThrift;
        logger.info("Client LongpollService was created with Thrift http client on url = {}", resultPath);
        transport = new THttpClient(resultPath);
        TProtocol protocol = new TJSONProtocol(transport);
        client = new LongpollThriftService.Client(protocol);
    }

    @Override
    public void sendDiagramPush(long diagramId) {
        try {
            client.sendDiagramPush(diagramId);
        } catch (TException e) {
            logger.error("Longpoll encountered error", e);
        }
    }
}
