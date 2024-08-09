function checkEmail() {
    var email = $('#email').val();

    $.ajax({
        url: '/check-email', // 이메일 중복 체크를 위한 서버 URL
        type: 'GET',
        data: { email: email },
        // controller에서 잡힌 exception이 여기로 옴
        // true든 false든 들어옴
        // controller에서 처리가 된 예외를 받음 -> success
        success: function(response) {
            if (response) {
                $('#emailCheckResult').text('사용 가능한 이메일입니다.').removeClass('text-danger').addClass('text-success');
                isEmailChecked = true;
                $('#signupButton').prop('disabled', false);  // 회원가입 버튼 활성화
            } else {
                $('#emailCheckResult').text('이미 사용 중인 이메일입니다.').removeClass('text-success').addClass('text-danger');
                isEmailChecked = false;
                $('#signupButton').prop('disabled', true);  // 회원가입 버튼 비활성화

            }
        },
        // controller에서 잡히지 않은 exception (처리하지 못한 예외 받아서 처리)
        error : function() {
            $('#emailCheckResult').text('유효하지 않은 이메일입니다.').addClass('text-danger');
            isEmailChecked = false;
            $('#signupButton').prop('disabled', true);  // 오류 발생 시 회원가입 버튼 비활성화
        }
    });
}

// 추가적으로 폼 제출 시 이메일 중복 체크가 완료되었는지 확인
$('form').on('submit', function(event) {
    if (!isEmailChecked) {
        event.preventDefault(); // 폼 제출 중단
        alert('이메일 중복 체크를 완료해주세요.');
    }
});
