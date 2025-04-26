class ChatApp {
    constructor() {
        this.stompClient = null;
        this.username = null;
        this.initialize();
    }

    initialize() {
        this.loginContainer = document.getElementById('login-container');
        this.chatContainer = document.getElementById('chat-container');
        this.usernameInput = document.getElementById('username');
        this.joinButton = document.getElementById('join-button');
        this.messageInput = document.getElementById('message-input');
        this.sendButton = document.getElementById('send-button');
        this.currentUserSpan = document.getElementById('current-user');
        this.messagesContainer = document.getElementById('messages');

        this.joinButton.addEventListener('click', () => this.handleLogin());
        this.usernameInput.focus();
        this.usernameInput.addEventListener('keypress', (e) => {
            if (e.key === 'Enter') this.handleLogin();
        });
    }

    handleLogin() {
        this.username = this.usernameInput.value.trim();
        if (!this.username) return;

        this.connectWebSocket();
    }

    connectWebSocket() {
        const socket = new SockJS('/gmarchev-chatroom');
        this.stompClient = Stomp.over(socket);

        this.stompClient.connect({}, (frame) => {
            this.showChatInterface();
            this.subscribeToChannels();
            this.sendJoinRequest();
        }, (error) => {
            console.error('Connection error:', error);
            this.showError('Connection failed. Trying to reconnect...');
            setTimeout(() => this.connectWebSocket(), 3000);
        });
    }

    showChatInterface() {
        this.loginContainer.style.display = 'none';
        this.chatContainer.style.display = 'block';
        this.currentUserSpan.textContent = this.username;
        this.messageInput.focus();
    }

    subscribeToChannels() {
        this.stompClient.subscribe('/user/queue/history', (response) => {
            const history = JSON.parse(response.body);
            history.reverse().forEach(msg => this.addMessage(msg));
        });

        this.stompClient.subscribe('/topic/public', (message) => {
            this.addMessage(JSON.parse(message.body));
        });

        this.sendButton.addEventListener('click', () => this.sendMessage());
        this.messageInput.addEventListener('keypress', (e) => {
            if (e.key === 'Enter') this.sendMessage();
        });
    }

    sendJoinRequest() {
        this.stompClient.send("/app/chat.addUser", {},
            JSON.stringify({ sender: this.username })
        );
    }

    sendMessage() {
        const content = this.messageInput.value.trim();
        if (!content) return;

        this.stompClient.send("/app/chat.sendMessage",
            {},
            JSON.stringify({
                sender: this.username,
                content: content
            })
        );
        this.messageInput.value = '';
    }

    addMessage(msg) {
        const messageElement = document.createElement('div');
        messageElement.className = `message ${msg.type !== 'CHAT' 
            ? 'system' 
            : (msg.sender === this.username ? 'personal' : '')}`;
        console.log('------------>');
        console.log(messageElement.className);
        console.log(msg.sender);
        console.log(username);
        console.log('<------------');

        messageElement.innerHTML = `
                    <div class="message-header">
                        <span>${msg.sender}</span>
                        <span>${this.formatTimestamp(msg.timestamp)}</span>
                    </div>
                    <div class="message-content">${msg.content}</div>
                `;

        this.messagesContainer.appendChild(messageElement);
        this.messagesContainer.scrollTop = this.messagesContainer.scrollHeight;
    }

    formatTimestamp(timestamp) {
        if (!timestamp) return '';
        const date = new Date(timestamp);
        return date.toLocaleTimeString([], {
            hour: '2-digit',
            minute: '2-digit',
            second: '2-digit'
        });
    }

    showError(message) {
        const errorElement = document.createElement('div');
        errorElement.className = 'error-message';
        errorElement.textContent = message;
        document.body.appendChild(errorElement);

        setTimeout(() => errorElement.remove(), 3000);
    }
}

// Initialize the application
window.addEventListener('DOMContentLoaded', () => new ChatApp());