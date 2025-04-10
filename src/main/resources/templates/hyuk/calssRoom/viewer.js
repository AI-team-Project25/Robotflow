// async function fetchSeatStatus() {
//     const res = await fetch('seats_status.json');
//     return await res.json();
//   }
  
//   function getRoomFromURL() {
//     const params = new URLSearchParams(window.location.search);
//     return params.get('room');
//   }
  
//   function createSeatBox(id, status) {
//     const div = document.createElement('div');
//     div.className = `seat-box ${status}`;
//     div.textContent = id;
//     return div;
//   }
  
//   async function renderRoomView() {
//     const roomName = getRoomFromURL();
//     if (!roomName) return;
  
//     document.getElementById('room-title').textContent = roomName;
//     const seats = (await fetchSeatStatus())[roomName];
//     const seatGrid = document.getElementById('seat-grid');
//     seatGrid.innerHTML = '';
  
//     const seatOrder = ['a', 'b', 'c'];
//     for (let row of seatOrder) {
//       for (let i = 1; i <= 4; i++) {
//         const seatId = `${row}${i}`;
//         const status = seats[seatId];
//         const box = createSeatBox(seatId, status);
//         seatGrid.appendChild(box);
//       }
//     }
  
//     // 영상 버튼 이벤트
//     document.getElementById('show-video-btn').addEventListener('click', () => {
//       const videoFrame = document.getElementById('video-frame');
//       const videoContainer = document.getElementById('video-container');
//       // IP 카메라 주소 설정 (가상의 주소 예시)
//       videoFrame.src = `http://ipcam.local/stream/${encodeURIComponent(roomName)}`;
//       videoContainer.style.display = 'block';
//     });
//   }
  
//   renderRoomView();
  