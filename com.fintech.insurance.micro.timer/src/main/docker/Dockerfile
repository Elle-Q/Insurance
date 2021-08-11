FROM java:8
MAINTAINER zhanglei@xinlebao.com.cn

VOLUME /data
WORKDIR /data
ADD com.fintech.insurance.micro.timer-1.0.0.jar app.jar
RUN bash -c 'touch app.jar'
EXPOSE 8010
CMD ["java", "-Dspring.profiles.active=test", "-jar", "app.jar"]