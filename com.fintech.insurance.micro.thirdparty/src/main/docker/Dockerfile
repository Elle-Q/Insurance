FROM java:8
MAINTAINER zhanglei@xinlebao.com.cn

VOLUME /data
WORKDIR /data
ADD com.fintech.insurance.micro.thirdparty-1.0.0.jar app.jar
RUN bash -c 'touch app.jar'
EXPOSE 8096
CMD ["java", "-Dspring.profiles.active=test", "-jar", "app.jar"]