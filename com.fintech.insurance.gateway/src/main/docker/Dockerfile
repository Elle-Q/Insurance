FROM java:8
MAINTAINER zhanglei@xinlebao.com.cn

VOLUME /data
WORKDIR /data
ADD com.fintech.insurance.gateway-1.0.0.jar app.jar
RUN bash -c 'touch app.jar'
EXPOSE 7073
CMD ["java", "-Dspring.profdockeriles.active=test", "-jar", "app.jar"]