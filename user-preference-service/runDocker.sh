docker rm -f user-preference-container
docker rmi user-preference-image

docker build --platform=linux/arm64 -t user-preference-image .

docker run -p 8080:8080 --name user-preference-container user-preference-image