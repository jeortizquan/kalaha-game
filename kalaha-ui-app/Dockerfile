FROM node:latest as base
MAINTAINER  Jorge Ortiz <jeortiz.quan@gmail.com>
RUN mkdir -p /app
WORKDIR /app
COPY package.json /app/
RUN npm install
RUN npm install react-scripts@4.0.3 -g
COPY . /app

FROM base as development
CMD ["npm","start"]
EXPOSE 3000

FROM base as build
RUN npm run build --production
RUN npm install serve -g

FROM base as production
RUN npm run build --production
RUN npm install serve -g
EXPOSE 5000
CMD serve -s build