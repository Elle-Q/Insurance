FROM java:8
MAINTAINER zhanglei@xinlebao.com.cn

VOLUME /data
WORKDIR /data
ADD com.fintech.insurance.eureka-1.0.0.jar app.jar
RUN bash -c 'touch app.jar'
EXPOSE 1111
CMD ["java", "-Dspring.profiles.active=test", "-jar", "app.jar"]