FROM java:8
MAINTAINER zhanglei@xinlebao.com.cn

VOLUME /data
WORKDIR /data
COPY simsun.ttf /usr/share/fonts/simsun.ttf
ADD com.fintech.insurance.micro.biz-1.0.0.jar app.jar
RUN bash -c 'touch app.jar'
EXPOSE 8091
CMD ["java", "-Dspring.profiles.active=test", "-jar", "app.jar"]