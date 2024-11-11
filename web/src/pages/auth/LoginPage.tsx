import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import BaseLayout from '~/components/layouts/BaseLayout';
import { PAGE_LIST } from '~/constants';
import useLogin from '~/hooks/api/auth/useLogin';

const LoginPage = () => {
  const navigate = useNavigate();
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  const mutation = useLogin();

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    mutation.mutate(
      { username, password },
      {
        onSuccess: () => {
          navigate(PAGE_LIST.home);
        },
        onError: () => {
          alert('로그인 실패!');
        },
      },
    );
  };

  return (
    <BaseLayout>
      <h2>로그인</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label htmlFor="username">사용자 이름:</label>
          <input
            type="text"
            id="username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
          />
        </div>
        <div>
          <label htmlFor="password">비밀번호:</label>
          <input
            type="password"
            id="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>
        <button type="submit" disabled={mutation.isPending}>
          {mutation.isPending ? '로그인 중...' : '로그인하기'}
        </button>
        {mutation.isError && <p>로그인 중 오류가 발생했습니다.</p>}
      </form>
    </BaseLayout>
  );
};

export default LoginPage;
