FROM node:14.9 as angular-built
WORKDIR /app
COPY package.json /app
RUN npm install
COPY . /app
RUN npm run build

FROM nginx:alpine
COPY nginx_local.config /etc/nginx/conf.d/default.conf
RUN rm -rf /usr/share/nginx/html/*
COPY --from=angular-built /app/dist/frontend-angular /usr/share/nginx/html
CMD [ "nginx", "-g", "daemon off;" ]