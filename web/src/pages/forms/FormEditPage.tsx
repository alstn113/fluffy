import { useParams } from 'react-router-dom';
import BaseLayout from '~/components/layouts/BaseLayout';

const FormEditPage = () => {
  const { id } = useParams() as { id: string };

  return <BaseLayout>FormEditPage {id}</BaseLayout>;
};

export default FormEditPage;
