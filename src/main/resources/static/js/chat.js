const url = 'http://localhost:8080';
let stompClient;
let senderId;
let dialogId = document.querySelector("#dialogId").textContent;
let recipientId = document.querySelector("#recipient").textContent;
let contentMsg = document.querySelector("#message");
let $chatHistoryList = $(".chat-messages");

function sendMsg() {
    let content = contentMsg.value.trim();
    if(content && stompClient) {
        var contentMessage = {
            dialog: dialogId,
            sender: senderId,
            recipient: recipientId,
            content: content,
            time: getCurrentTime()
        }
        stompClient.send("/app/chat/" + recipientId, {}, JSON.stringify(contentMessage));
        contentMsg.value = '';
        var template = Handlebars.compile($("#message-template").html());
        $chatHistoryList.append(template(contentMessage));
    }
}


$.get("/getSender", function (response) {
    senderId = response;
    connectToChat(response);
});

function connectToChat(senderId) {
    console.log("connecting to chat...")
    let socket = new SockJS(url + '/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log("connected to: " + frame);
        stompClient.subscribe("/topic/messages/" + senderId, function (response) {
            let data = JSON.parse(response.body);
            if (recipientId == data.sender) {
                getMessage(data.sender, data.content, data.time);
            }
        });
    });
}

function getMessage(senderId, content, time) {
    $.get("/getNameSender/" + senderId, function (response) {
        var templateResponse = Handlebars.compile($("#message-response-template").html());
        var responseMessage = {
            name: response,
            time: time,
            content: content
        }
        $chatHistoryList.append(templateResponse(responseMessage));
        console.log(responseMessage);
    });
}

function getCurrentTime() {
    return new Date().toLocaleTimeString().replace(/([\d]+:[\d]{2})(:[\d]{2})(.*)/, "$1$3");
}