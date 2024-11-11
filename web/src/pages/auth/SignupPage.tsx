import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import BaseLayout from '~/components/layouts/BaseLayout';
import { PAGE_LIST } from '~/constants';
import useLogin from '~/hooks/api/auth/useLogin';

const SignupPage = () => {
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
          navigate(PAGE_LIST.auth.login);
        },
      },
    );
  };

  return (
    <BaseLayout>
      <h2>회원가입</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label htmlFor="username">username: </label>
          <input
            type="text"
            id="username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
          />
        </div>
        <div>
          <label htmlFor="password">password: </label>
          <input
            type="password"
            id="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>
        <button type="submit" disabled={mutation.isPending}>
          {mutation.isPending ? '가입 중...' : '가입하기'}
        </button>
        {mutation.isError && <p>회원가입 중 오류가 발생했습니다.</p>}
      </form>
    </BaseLayout>
  );
};

export default SignupPage;
